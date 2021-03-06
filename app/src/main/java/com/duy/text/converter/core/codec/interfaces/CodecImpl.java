/*
 * Copyright (c) 2017 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.text.converter.core.codec.interfaces;

import android.content.Context;

/**
 * Created by Duy on 16-Dec-17.
 */

public abstract class CodecImpl implements Codec {

    protected int max = 0;
    protected int confident = 0;

    @Override
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    protected void setMax(char[] arr) {
        setMax(arr.length);
    }

    protected void setMax(Object[] arr) {
        setMax(arr.length);
    }

    @Override
    public int getConfident() {
        return confident;
    }

    public void setConfident(int confident) {
        this.confident = confident;
    }

    public void incConfident() {
        confident++;
    }

    @Override
    public String getName(Context context) {
        return null;
    }
}
