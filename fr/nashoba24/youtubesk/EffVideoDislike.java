package fr.nashoba24.youtubesk;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class EffVideoDislike extends Effect {
	
	private Expression<Video> video;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean, ParseResult paramParseResult) {
		video = (Expression<Video>) expr[0];
		return true;
	}
	
	@Override
	public String toString(@Nullable Event e, boolean b) {
		return "dislike video";
	}
	
	@Override
	protected void execute(Event e) {
		if(YoutubeSK.ytb!=null) {
			try {
				YouTube.Videos.Rate rate = YoutubeSK.ytb.videos().rate(video.getSingle(e).getId(), "dislike");
				rate.buildHttpRequest().execute();
			}
			catch(Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
