package limelight.util;

import java.util.HashMap;
import java.util.Map;

public class OptionsMap extends HashMap<String, Object>
{
  public OptionsMap()
  {
    super();
  }

  public OptionsMap(Map<Object, Object> starter)
  {
    this();
    for(Map.Entry<Object, Object> entry : starter.entrySet())
      put(toKey(entry.getKey()), entry.getValue());
  }

  @Override
  public Object get(Object key)
  {
    if(key == null)
      return null;
    return super.get(toKey(key));
  }

  @Override
  public boolean containsKey(Object key)
  {
    return key != null && super.containsKey(toKey(key));
  }

  @Override
  public Object remove(Object key)
  {
    if(key == null)
      return null;
    return super.remove(toKey(key));
  }
  
  private String toKey(Object key)
  {
    if(key instanceof String)
      return (String)key;
    else
    {
      String value = key.toString();
      if(value.startsWith(":"))
        return value.substring(1);
      else
        return value;
    }
  }
}