package org.robotrader.quote.stocklytics;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.robotrader.quote.domain.Quote;

public class ObjectMapperTest {
	
	private final static String EXAMPLE = "{  \"name\" : { \"first\" : \"Joe\", \"last\" : \"Sixpack\" }, \"gender\" : \"MALE\", \"verified\" : false, \"userImage\" : \"Rm9vYmFyIQ==\" }";
	
	private final static String BLA = "{\"2011-01-03\":{\"open\":\"325.6400\",\"close\":\"329.5700\",\"high\":\"330.2600\",\"low\":\"324.8400\",\"volume\":\"15883600\"}}";

	private final static String BLA2 = "{\"test\":{\"open\":\"325.6400\",\"close\":\"329.5700\",\"high\":\"330.2600\",\"low\":\"324.8400\",\"volume\":\"15883600\"}}";
	
	@Test
	public void foo() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Quote quote = objectMapper.readValue(new ByteArrayInputStream(BLA2.getBytes()), Quote.class);
		System.out.print(quote.getTest().getOpen());
	}
	
	@Test
	public void foo2() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		User user = objectMapper.readValue(new ByteArrayInputStream(EXAMPLE.getBytes()), User.class);
	}
	
	@Test
	public void foo3() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String,Object> userData = objectMapper.readValue(new ByteArrayInputStream(BLA.getBytes()), Map.class);
		System.out.println(ToStringBuilder.reflectionToString(userData, ToStringStyle.DEFAULT_STYLE));
	}

}
