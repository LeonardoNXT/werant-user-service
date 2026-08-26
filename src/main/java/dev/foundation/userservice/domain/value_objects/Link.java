package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.errors.ValueObjectException;

import java.net.URI;
import java.net.URL;

public class Link {
    private final URL url;

    private Link(URL url){
        this.url = url;
    }

    public static Link create(String link) throws ValueObjectException {
        try{
            URI uri = new URI(link);
            URL url = uri.toURL();

            return new Link(url);
        }catch (Exception e) {
            throw new ValueObjectException(e.getMessage(), Link.class.getSimpleName(), "Invalid Argument");
        }
    }

    public URL value(){
        return this.url;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Link linkVo))return false;
        return this.url.toString().equals(linkVo.value().toString());
    }

    @Override
    public int hashCode(){
        return this.url.toString().hashCode();
    }
}
