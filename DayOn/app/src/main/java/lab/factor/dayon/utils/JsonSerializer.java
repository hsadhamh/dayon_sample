package lab.factor.dayon.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class JsonSerializer {
	
	ObjectMapper mObjMapper = null;
	
	static JsonSerializer mSerializer = null;
	public static JsonSerializer getInstance(){
		if(mSerializer == null)
			mSerializer = new JsonSerializer();
		return mSerializer;
	}
	
	public JsonSerializer(){
		mObjMapper = new ObjectMapper();
		mObjMapper.setSerializationInclusion(Include.NON_NULL);
		mObjMapper.setVisibility(mObjMapper.getSerializationConfig().getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
	}	
	
	public String SerializeToString(Object obj, boolean printPretty) 
			throws JsonProcessingException, UnsupportedEncodingException{
		
		if(printPretty)
			return mObjMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		
		return mObjMapper.writeValueAsString(obj);
	}
	
	public <T> Object UnserializeToObject(String sJson, Class<T> valueType) 
			throws JsonParseException, JsonMappingException, IOException{
		return mObjMapper.readValue(sJson, valueType);
	}

	public <T> List<T> ConvertToObjectList(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		List<T> ts = mapper.readValue(json, listType);
		return ts;
	}

	public ObjectMapper getMapper() { return mObjMapper; }
}
