package fr.nashoba24.youtubesk;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import com.google.api.services.youtube.model.Video;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

public class ExprVideoFavoriteCount extends SimpleExpression<Long>{
	
	private Expression<Video> video;
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
	@Override
	public Class<? extends Long> getReturnType() {
		return Long.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean, ParseResult paramParseResult) {
		video = (Expression<Video>) expr[0];
		return true;
	}
	
	@Override
	public String toString(@Nullable Event e, boolean paramBoolean) {
		return "favorite count of video";
	}
	
	@Override
	@Nullable
	protected Long[] get(Event e) {
		if(YoutubeSK.ytb!=null) {
			try {
				return new Long[]{ video.getSingle(e).getStatistics().getFavoriteCount().longValue() };
			}
			catch(Exception e1) {
				return null;
			}
		}
		return null;
	}
}
