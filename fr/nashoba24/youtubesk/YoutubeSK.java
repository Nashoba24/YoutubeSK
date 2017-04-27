package fr.nashoba24.youtubesk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.registrations.Classes;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

public class YoutubeSK extends JavaPlugin {
	
	private static YoutubeSK instance;
	private static final String APPLICATION_NAME = "YoutubeSK";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT;
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private static File DATA_STORE_DIR = null;
    private static final List<String> SCOPES = Arrays.asList(YouTubeScopes.YOUTUBE_READONLY);
	public static YouTube ytb = null;
	
	@Override
	public void onEnable() {
		instance = this;
		File file = new File(YoutubeSK.getInstance().getDataFolder() + "/");
		if(!file.exists()) {
			file.mkdir();
		}
        try {
        	DATA_STORE_DIR = new File(YoutubeSK.getInstance().getDataFolder() + "/");
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("Critical error in YoutubeSK, the plugin has been disabled");
            this.getPluginLoader().disablePlugin(this);
        }
        
        registerAll();
        
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&aYoutubeSK Enabled!"));
		
	}

	@Override
	public void onDisable() {
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&bYoutubeSK Disabled!"));
	}
	
	public static YoutubeSK getInstance() {
		return YoutubeSK.instance;
	}
	
    public static Credential authorize() throws IOException {
    	File file = new File(YoutubeSK.getInstance().getDataFolder() + "/client_secret.json");
        InputStream in = new FileInputStream(file);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }
    
    public static YouTube getYouTubeService() throws IOException {
        Credential credential = authorize();
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    public static Video getVideo(String id) {
    	YouTube.Videos.List videoRequest;
		try {
			videoRequest = ytb.videos().list("snippet,statistics,contentDetails");
	    	videoRequest.setId(id);
	    	VideoListResponse listResponse = videoRequest.execute();
	    	List<Video> videoList = listResponse.getItems();

	    	return videoList.iterator().next();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public static void registerAll() {
    	Classes.registerClass(new ClassInfo<Video>(Video.class, "video").user("video").name("video").parser(new Parser<Video>() {

		@Override
		public String getVariableNamePattern() {
			return ".+";
		}

		@Override
		public String toString(Video arg0, int arg1) {
			return arg0.getId();
		}

		@Override
		public String toVariableNameString(Video arg0) {
			return arg0.getId();
		}
	   
	   }));
	        
    	Skript.registerEffect(EffYoutubeConnect.class, "connect to youtube", "youtube connect");
    	Skript.registerExpression(ExprVideoViewCount.class, Long.class, ExpressionType.PROPERTY, "view[s][ count] of %video%"); 
    	Skript.registerExpression(ExprVideoWithId.class, Video.class, ExpressionType.PROPERTY, "video[ (with|from) id] %string%");
    	Skript.registerExpression(ExprVideoDislikeCount.class, Long.class, ExpressionType.PROPERTY, "dislike[s][ count] of %video%");
    	Skript.registerExpression(ExprVideoLikeCount.class, Long.class, ExpressionType.PROPERTY, "like[s][ count] of %video%");
    	Skript.registerExpression(ExprVideoCommentCount.class, Long.class, ExpressionType.PROPERTY, "comment[s][ count] of %video%");
    	Skript.registerExpression(ExprVideoFavoriteCount.class, Long.class, ExpressionType.PROPERTY, "favorite[s][ count] of %video%");
    	Skript.registerExpression(ExprVideoCaption.class, String.class, ExpressionType.PROPERTY, "caption of %video%");
    	Skript.registerExpression(ExprVideoDefinition.class, String.class, ExpressionType.PROPERTY, "definition of %video%");
    	Skript.registerExpression(ExprVideoDimension.class, String.class, ExpressionType.PROPERTY, "dimension of %video%"); //2D or 3D
    	Skript.registerExpression(ExprVideoDuration.class, String.class, ExpressionType.PROPERTY, "duration of %video%");
    	Skript.registerExpression(ExprVideoId.class, String.class, ExpressionType.PROPERTY, "id of %video%");
    	Skript.registerEffect(EffVideoLike.class, "like %video%");
    	Skript.registerEffect(EffVideoDislike.class, "dislike %video%");
    	
    }
}
