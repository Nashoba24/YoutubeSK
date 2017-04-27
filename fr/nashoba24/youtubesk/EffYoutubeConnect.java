package fr.nashoba24.youtubesk;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class EffYoutubeConnect extends Effect {
	
	@Override
	public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean, ParseResult paramParseResult) {
		return true;
	}
	
	@Override
	public String toString(@Nullable Event e, boolean b) {
		return "connect to youtube";
	}
	
	@Override
	protected void execute(Event e) {
		File file = new File(YoutubeSK.getInstance().getDataFolder() + "/client_secret.json");
		if(file.exists()) {
			try {
				YoutubeSK.ytb = YoutubeSK.getYouTubeService();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("YoutubeSK can't connect to your Youtube account!");
			}
		}
		else {
			System.out.println("YoutubeSk cannot find the file client_secret.json!");
		}
	}
}
