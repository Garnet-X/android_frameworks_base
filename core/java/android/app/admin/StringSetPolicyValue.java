/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.app.admin;

import static android.app.admin.flags.Flags.devicePolicySizeTrackingEnabled;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.os.Parcel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @hide
 */
public final class StringSetPolicyValue extends PolicyValue<Set<String>> {

    public StringSetPolicyValue(@NonNull Set<String> value) {
        super(value);
        if (devicePolicySizeTrackingEnabled()) {
            for (String str : value) {
                PolicySizeVerifier.enforceMaxStringLength(str, "policyValue");
            }
        }
    }

    public StringSetPolicyValue(Parcel source) {
        this(readValues(source));
    }

    private static Set<String> readValues(Parcel source) {
        Set<String> values = new HashSet<>();
        int size = source.readInt();
        for (int i = 0; i < size; i++) {
            values.add(source.readString());
        }
        return values;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringSetPolicyValue other = (StringSetPolicyValue) o;
        return Objects.equals(getValue(), other.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return "StringSetPolicyValue { " + getValue() + " }";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(getValue().size());
        for (String entry : getValue()) {
            dest.writeString(entry);
        }
    }

    @NonNull
    public static final Creator<StringSetPolicyValue> CREATOR =
            new Creator<StringSetPolicyValue>() {
                @Override
                public StringSetPolicyValue createFromParcel(Parcel source) {
                    return new StringSetPolicyValue(source);
                }

                @Override
                public StringSetPolicyValue[] newArray(int size) {
                    return new StringSetPolicyValue[size];
                }
            };
}
