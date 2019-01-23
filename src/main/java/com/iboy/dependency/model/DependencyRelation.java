package com.iboy.dependency.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * ビルドモデルにおけるプロジェクトの依存関係を表すクラス.
 *
 * @author 12yoh
 * @since 0.0.1
 */
@AllArgsConstructor
@EqualsAndHashCode
public class DependencyRelation {

	/**
	 * 依存元
	 */
	@Setter
	@Getter
	private ProjectModel from;

	/**
	 * 依存先
	 */
	@Setter
	@Getter
	private ProjectModel to;
}
