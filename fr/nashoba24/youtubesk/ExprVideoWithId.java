package fr.nashoba24.youtubesk;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import com.google.api.services.youtube.model.Video;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

public class ExprVideoWithId extends SimpleExpression<Video>{
	
	private Expression<String> id;
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
	@Override
	public Class<? extends Video> getReturnType() {
		return Video.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean, ParseResult paramParseResult) {
		id = (Expression<String>) expr[0];
		return true;
	}
	
	@Override
	public String toString(@Nullable Event e, boolean paramBoolean) {
		return "video with id";
	}
	
	@Override
	@Nullable
	protected Video[] get(Event e) {
		if(YoutubeSK.ytb!=null) {
			try {
				return new Video[]{ YoutubeSK.getVideo(id.getSingle(e)) };
			}
			catch(Exception e1) {
				return null;
			}
		}
		return null;
	}
}
