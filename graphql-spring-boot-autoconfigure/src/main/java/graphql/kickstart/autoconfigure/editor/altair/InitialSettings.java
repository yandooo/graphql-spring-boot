package graphql.kickstart.autoconfigure.editor.altair;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class InitialSettings {

  private String theme;
  private String language;
  private Integer addQueryDepthLimit;
  private Integer tabSize;
  private Boolean enableExperimental;
  @JsonProperty("theme.fontsize")
  private Integer themeFontSize;
  @JsonProperty("theme.editorFontFamily")
  private String themeEditorFontFamily;
  @JsonProperty("theme.editorFontSize")
  private Integer themeEditorFontSize;
  private Boolean disablePushNotification;
  @JsonProperty("plugin.list")
  private List<String> pluginList;
  @JsonProperty("request.withCredentials")
  private Boolean requestWithCredentials;
  @JsonProperty("schema.reloadOnStart")
  private Boolean schemaReloadOnStart;
  @JsonProperty("alert.disableWarnings")
  private Boolean alertDisableWarnings;
  @JsonProperty("history.depth")
  private Integer historyDepth;

  @JsonProperty("response.hideExtensions")
  private Boolean responseHideExtensions;
}
