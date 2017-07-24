package org.mybatis.guice.provision;

import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.matcher.AbstractMatcher;

public final class KeyMatcher <T> extends AbstractMatcher<Binding<?>> {
  private final Key<T> key;
  KeyMatcher(Key<T> key){
    this.key = key;
  }
  @Override
  public boolean matches(Binding<?> t) {
    return key.equals(t.getKey());
  }
  
  public static <T> KeyMatcher<T> create(Key<T> key){
    return new KeyMatcher<T>(key);
  }
}
