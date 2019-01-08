package de.adorsys.psd2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * The payment initiation has failed due to a missing KID. This is a specific message code for the Norwegian market,
 * where ASPSP can require the payer to transmit the KID.
 */
@ApiModel(description = "The payment initiation has failed due to a missing KID. This is a specific message code for " +
    "the Norwegian market, where ASPSP can require the payer to transmit the KID. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-01-08T12:03" +
    ":25.103+01:00[Europe/Berlin]")

public class TppMessagePISREQUIREDKIDMISSING401 {
    @JsonProperty("category")
    private TppMessageCategory category = null;
    @JsonProperty("code")
    private CodeEnum code = null;
    @JsonProperty("path")
    private String path = null;
    @JsonProperty("text")
    private String text = null;

    public TppMessagePISREQUIREDKIDMISSING401 category(TppMessageCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Get category
     *
     * @return category
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid

    public TppMessageCategory getCategory() {
        return category;
    }

    public void setCategory(TppMessageCategory category) {
        this.category = category;
    }

    public TppMessagePISREQUIREDKIDMISSING401 code(CodeEnum code) {
        this.code = code;
        return this;
    }

    /**
     * Get code
     *
     * @return code
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public CodeEnum getCode() {
        return code;
    }

    public void setCode(CodeEnum code) {
        this.code = code;
    }

    public TppMessagePISREQUIREDKIDMISSING401 path(String path) {
        this.path = path;
        return this;
    }

    /**
     * Get path
     *
     * @return path
     **/
    @ApiModelProperty(value = "")

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TppMessagePISREQUIREDKIDMISSING401 text(String text) {
        this.text = text;
        return this;
    }

    /**
     * Get text
     *
     * @return text
     **/
    @ApiModelProperty(value = "")

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TppMessagePISREQUIREDKIDMISSING401 tppMessagePISREQUIREDKIDMISSING401 = (TppMessagePISREQUIREDKIDMISSING401) o;
        return Objects.equals(this.category, tppMessagePISREQUIREDKIDMISSING401.category) &&
            Objects.equals(this.code, tppMessagePISREQUIREDKIDMISSING401.code) &&
            Objects.equals(this.path, tppMessagePISREQUIREDKIDMISSING401.path) &&
            Objects.equals(this.text, tppMessagePISREQUIREDKIDMISSING401.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, code, path, text);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TppMessagePISREQUIREDKIDMISSING401 {\n");

        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        sb.append("    text: ").append(toIndentedString(text)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /**
     * Gets or Sets code
     */
    public enum CodeEnum {
        MISSING("REQUIRED_KID_MISSING");

        private String value;

        CodeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static CodeEnum fromValue(String text) {
            for (CodeEnum b : CodeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }
}

