package com.novarto.jackson.fj;

public class SimpleBean
    {
        public final String a;
        public final int b;

        public SimpleBean(String a, int b)
        {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            SimpleBean that = (SimpleBean) o;

            if (b != that.b)
            {
                return false;
            }
            return !(a != null ? !a.equals(that.a) : that.a != null);

        }

        @Override
        public int hashCode()
        {
            int result = a != null ? a.hashCode() : 0;
            result = 31 * result + b;
            return result;
        }
    }