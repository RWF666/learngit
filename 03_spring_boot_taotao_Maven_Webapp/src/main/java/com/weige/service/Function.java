package com.weige.service;

import com.sun.glass.ui.MenuItem.Callback;

public interface Function<E,T> {
	public T Callback(E e);
}
