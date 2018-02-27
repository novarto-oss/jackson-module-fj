package com.novarto.jackson.fj.util;

import fj.function.Effect0;
import fj.function.TryEffect0;

public class Util
{
    public static Effect0 rethrow(TryEffect0<Exception> body)
    {
        return () ->
        {
            try
            {
                body.f();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        };
    }
}
