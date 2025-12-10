package com.micropos.model;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    ToSend,
    ToCancel,
    Delivering,
    Completed,
    Canceled
}
