/**
 * Copyright 2015 The Javaslang Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.novarto.jackson.fj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.novarto.jackson.fj.deserialize.FjDeserializers;
import com.novarto.jackson.fj.serialize.FjSerializers;

import java.io.Serializable;

public class FjModule extends SimpleModule
{
    private static final long serialVersionUID = 1L;

    public static class Options implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private boolean plainOption = true;

        public Options plainOption(boolean value)
        {
            plainOption = value;
            return this;
        }

        public boolean plainOption()
        {
            return plainOption;
        }
    }

    private final Options options;

    public FjModule()
    {
        this(new Options());
    }

    public FjModule(Options options)
    {
        this.options = options;
    }

    @Override
    public void setupModule(SetupContext context)
    {
        super.setupModule(context);
        new ParameterNamesModule(JsonCreator.Mode.PROPERTIES).setupModule(context);
        context.addSerializers(new FjSerializers(options));
        context.addDeserializers(new FjDeserializers(options));

    }
}
