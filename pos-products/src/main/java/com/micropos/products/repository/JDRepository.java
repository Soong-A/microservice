//package com.micropos.products.repository;
//
//import com.micropos.model.Product;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Repository;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//@Repository
//public class JDRepository implements ProductRepository {
//    private List<Product> products = null;
//
//    @Override
//    @Cacheable("products")
//    public List<Product> allProducts() {
//        try {
//            if (products == null)
//                products = parseJD("Java");
//        } catch (IOException e) {
//            products = new ArrayList<>();
//        }
//        return products;
//    }
//
//    @Override
//    public Product findProduct(String productId) {
//        for (Product p : allProducts()) {
//            if (p.getId().equals(productId)) {
//                return p;
//            }
//        }
//        return null;
//    }
//
//    public static List<Product> parseJD(String keyword) throws IOException {
//        String url = "https://search.jd.com/Search?keyword=" + keyword;
//        Document document = Jsoup.parse(new URL(url), 10000);
//        Element element = document.getElementById("J_goodsList");
//        if (element == null) {
//            System.err.println("警告: 未找到商品列表元素，京东页面结构可能已变更");
//            return new ArrayList<>(); // 返回空列表而不是null
//        }
//        Elements elements = element.getElementsByTag("li");
//        List<Product> list = new ArrayList<>();
//
//        for (Element el : elements) {
//            String id = el.attr("data-spu");
//            String img = "https:".concat(el.getElementsByTag("img").eq(0).attr("data-lazy-img"));
//            String price = el.getElementsByAttribute("data-price").text();
//            String title = el.getElementsByClass("p-name").eq(0).text();
//            if (title.contains("，"))
//                title = title.substring(0, title.indexOf("，"));
//
//            Product product = new Product(id, title, Double.parseDouble(price), img);
//            list.add(product);
//        }
//        return list;
//    }
//}

package com.micropos.products.repository;


import com.micropos.model.Product;
import com.microsoft.playwright.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JDRepository implements ProductRepository {
    private List<Product> products = null;

    @Override
    @Cacheable("products")
    public List<Product> allProducts() {
        try {
            if (products == null) {
                System.out.println("开始通过浏览器模拟从京东获取数据...");
                products = parseJDWithPlaywright("Java");

                if (products == null || products.isEmpty()) {
                    System.err.println("浏览器模拟采集失败，使用测试数据。");
                    products = createTestProducts();
                } else {
                    System.out.println("成功采集到 " + products.size() + " 个商品。");
                }
            }
        } catch (Exception e) {
            System.err.println("获取商品列表时发生异常: " + e.getMessage());
            e.printStackTrace();
            products = createTestProducts();
        }
        return products;
    }

    @Override
    public Product findProduct(String productId) {
        for (Product p : allProducts()) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    /**
     * 使用 Playwright 模拟浏览器访问京东
     * 核心：通过持久化上下文保存登录状态，避免每次都需要登录
     */
    private List<Product> parseJDWithPlaywright(String keyword) {
        List<Product> productList = new ArrayList<>();
        BrowserContext context = null;
        Playwright playwright = null;

        try {
            playwright = Playwright.create();
            Path userDataDir = Paths.get("./jd_browser_data");
            System.out.println("浏览器用户数据将保存至: " + userDataDir.toAbsolutePath());

            BrowserType.LaunchPersistentContextOptions options = new BrowserType.LaunchPersistentContextOptions()
                    .setHeadless(true) // 【调试时请先设为false，确认能采集到数据后再改回true】
                    .setViewportSize(1920, 1080)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .setTimeout(120000)
                    .setArgs(List.of("--disable-blink-features=AutomationControlled"));

            context = playwright.chromium().launchPersistentContext(userDataDir, options);
            Page page = context.newPage();

            String url = "https://search.jd.com/Search?keyword=" + keyword + "&enc=utf-8";
            System.out.println("正在通过浏览器访问: " + url);
            page.navigate(url);

            // 等待页面主要内容加载
            page.waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED);
            page.waitForTimeout(5000);
            System.out.println("页面加载完成，当前标题: " + page.title());

            // 【核心修改】针对新页面结构的滚动与等待
            System.out.println("开始模拟滚动以加载更多商品...");
            // 新页面可能采用更现代的懒加载，需要更充分的滚动
            for (int i = 1; i <= 6; i++) {
                page.evaluate("window.scrollTo(0, document.body.scrollHeight * " + i + "/6)");
                page.waitForTimeout(2500 + (long) (Math.random() * 1500));
            }
            page.waitForTimeout(4000); // 滚动后多等待一会儿

            // 【核心修改】使用基于你提供HTML的新选择器
            System.out.println("正在尝试查找商品卡片...");
            // 主要选择器：商品卡片的外层包装器
            List<ElementHandle> itemElements = page.querySelectorAll("[data-sku].plugin_goodsCardWrapper, div[class*='goodsCardWrapper']");

            if (itemElements.isEmpty()) {
                // 备用选择器：尝试查找任何包含data-sku属性的div
                itemElements = page.querySelectorAll("div[data-sku]");
                System.out.println("使用备用选择器找到 " + itemElements.size() + " 个元素。");
            }

            System.out.println("找到商品卡片数量: " + itemElements.size());

            if (itemElements.isEmpty()) {
                System.err.println("【警告】未找到任何商品卡片。页面结构可能再次变化。");
                // 再次保存页面用于调试
                try {
                    String pageContent = page.content();
                    Files.write(Paths.get("jd_latest_debug.html"), pageContent.getBytes(StandardCharsets.UTF_8));
                    System.err.println("最新页面结构已保存至: jd_latest_debug.html");
                } catch (IOException e) {
                    System.err.println("保存调试文件失败: " + e.getMessage());
                }
                return null;
            }

            // 解析每个商品卡片
            int successCount = 0;
            for (ElementHandle item : itemElements) {
                try {
                    // 1. 获取商品ID (data-sku属性)
                    String id = item.getAttribute("data-sku");
                    if (id == null || id.isEmpty()) {
                        continue; // 没有ID的商品跳过
                    }

                    // 2. 获取商品图片 (新选择器)
                    String imgUrl = "";
                    // 尝试多种图片选择器，优先取data-src，然后是src
                    ElementHandle imgEl = item.querySelector("img[data-src], img._img_18s24_1, img._goods_img_puhgn_56");
                    if (imgEl != null) {
                        imgUrl = imgEl.getAttribute("data-src");
                        if (imgUrl == null || imgUrl.isEmpty()) {
                            imgUrl = imgEl.getAttribute("src");
                        }
                        // 确保有完整的URL
                        if (imgUrl != null && imgUrl.startsWith("//")) {
                            imgUrl = "https:" + imgUrl;
                        }
                    }
                    // 如果没找到图片，设置一个默认的
                    if (imgUrl == null || imgUrl.isEmpty()) {
                        imgUrl = "https://img14.360buyimg.com/n1/s200x200_jfs/t1/110171/14/10737/56749/5e81d76aE8c0b9c30/032a3b6c30cae6a2.jpg";
                    }

                    // 3. 获取商品价格 (新选择器)
                    String priceText = "0";
                    ElementHandle priceEl = item.querySelector("span._price_uqsva_14, ._price_puhgn_60, [class*='price']");
                    if (priceEl != null) {
                        // 新价格结构可能是分开的，需要获取所有文本
                        priceText = priceEl.textContent().trim();
                        // 清理文本，只保留数字和小数点
                        priceText = priceText.replaceAll("[^0-9.]", "");
                        // 处理可能出现的“¥38.1”这种格式，或者“38.1”
                    }

                    // 4. 获取商品标题 (新选择器)
                    String title = "";
                    ElementHandle titleEl = item.querySelector("._goods_title_container_1g56m_1, ._text_1g56m_31, [title]");
                    if (titleEl != null) {
                        title = titleEl.textContent().trim();
                        if (title.isEmpty()) {
                            // 如果文本为空，尝试获取title属性
                            title = titleEl.getAttribute("title");
                        }
                    }

                    // 清理标题
                    if (title != null) {
                        title = title.replace("广告", "")
                                .replace("\n", " ")
                                .replaceAll("\\s+", " ")
                                .trim();
                    }

                    // 5. 创建商品对象
                    if (title != null && !title.isEmpty() && priceText != null && !priceText.isEmpty() && !priceText.equals("0")) {
                        try {
                            double price = Double.parseDouble(priceText);
                            Product product = new Product(id, title, price, imgUrl);
                            productList.add(product);
                            successCount++;
                            System.out.println("解析成功: " + title + " | 价格: ¥" + price + " | ID: " + id);
                        } catch (NumberFormatException e) {
                            System.err.println("价格转换失败，跳过商品。标题: " + title + "， 价格文本: \"" + priceText + "\"");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("解析单个商品卡片时出错 (ID可能: " + item.getAttribute("data-sku") + "): " + e.getMessage());
                }
            }

            System.out.println("商品解析完成。总计尝试: " + itemElements.size() + "， 成功解析: " + successCount);

        } catch (Exception e) {
            System.err.println("Playwright采集过程中发生严重错误: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (context != null) context.close();
            if (playwright != null) playwright.close();
        }

        return productList;
    }
    /**
     * 创建测试数据（当京东采集失败时使用）
     */
    private List<Product> createTestProducts() {
        List<Product> testProducts = new ArrayList<>();
        testProducts.add(new Product("1001", "Java核心技术 卷I（原书第11版）", 149.0, "https://img14.360buyimg.com/n1/s200x200_jfs/t1/117913/11/24236/119582/625fdf2aE9e61d5ce/99a49a2e0de9c2c5.jpg"));
        testProducts.add(new Product("1002", "Spring Boot实战：构建现代Java应用", 89.0, "https://img14.360buyimg.com/n1/s200x200_jfs/t1/180003/11/13141/56623/60e467b8E032b0321/04e0b6c5e92d9cbc.jpg"));
        testProducts.add(new Product("1003", "Spring Cloud微服务架构实战", 99.0, "https://img14.360buyimg.com/n1/s200x200_jfs/t1/128359/22/20544/76919/625fdf2aE36d2c177/ddfe6f60d6ce02ec.jpg"));
        testProducts.add(new Product("1004", "MySQL必知必会（第5版）", 49.0, "https://img14.360buyimg.com/n1/s200x200_jfs/t1/93647/33/26139/57330/625fdf2aE7bfdd697/cd55a0ad40f34b68.jpg"));
        testProducts.add(new Product("1005", "Effective Java（原书第3版）中文版", 118.0, "https://img14.360buyimg.com/n1/s200x200_jfs/t1/113827/39/20007/65575/625dbb77Ea50bb4a5/2dfe7f2b5ba5d5ec.jpg"));
        testProducts.add(new Product("1006", "深入理解Java虚拟机（第3版）", 129.0, "https://img14.360buyimg.com/n1/s200x200_jfs/t1/172752/39/32467/150386/63de3c38F3e16fea8/51d5c8ba42a26280.jpg"));
        testProducts.add(new Product("1007", "Java并发编程实战", 88.0, "https://img14.360buyimg.com/n1/s200x200_jfs/t1/129832/15/22992/114635/6243db3dE6fba4d7a/6d75b736775cc184.jpg"));
        testProducts.add(new Product("1008", "阿里巴巴Java开发手册（终极版）", 45.0, "https://img14.360buyimg.com/n1/s200x200_jfs/t1/135785/27/23881/92921/625fdf2aE523362ef/45c31bf25a792547.jpg"));
        testProducts.add(new Product("1009", "Head First Java（中文版第3版）", 79.0, "https://img14.360buyimg.com/n1/s200x200_jfs/t1/175281/30/33263/126315/63de3c39F3d9399d0/0dd7c55e1836835d.jpg"));
        testProducts.add(new Product("1010", "Java编程思想（第5版）", 138.0, "https://img14.360buyimg.com/n1/s200x200_jfs/t1/201022/31/33210/184553/63de3c38F40bce75d/278369af1a3c28ee.jpg"));

        System.out.println("已生成 " + testProducts.size() + " 个测试商品");
        return testProducts;
    }
}