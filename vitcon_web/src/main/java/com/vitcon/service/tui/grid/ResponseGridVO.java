package com.vitcon.service.tui.grid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"result",
"data",
"messae"
})
public class ResponseGridVO<T>{

@JsonProperty("result")
private Boolean result;
@JsonProperty("data")
private Data data;
@JsonProperty("message")
private String message;


@JsonProperty("result")
public Boolean getResult() {
return result;
}

@JsonProperty("result")
public void setResult(Boolean result) {
this.result = result;
}

@JsonProperty("data")
public Data getData() {
return data;
}

@JsonProperty("data")
public void setData(Data data) {
this.data = data;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

}