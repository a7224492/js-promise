package com.kodgames.battleserver.service.battle.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 规则注解，在构建地区规则玩法时候使用，具体使用方法，参考Rules_GuangDong.java */

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BattleRulesAnnotation
{
	public String comment() default "comment";

	public boolean isArea() default false;
}
