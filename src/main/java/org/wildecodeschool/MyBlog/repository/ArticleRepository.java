package org.wildecodeschool.MyBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildecodeschool.MyBlog.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
