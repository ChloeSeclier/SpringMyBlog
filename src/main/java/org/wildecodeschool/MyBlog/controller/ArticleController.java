package org.wildecodeschool.MyBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildecodeschool.MyBlog.model.Article;
import org.wildecodeschool.MyBlog.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles(){
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id){
        Article article = articleRepository.findById(id).orElse(null);
        if(article==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article articleToCreate){
        articleToCreate.setCreatedAt(LocalDateTime.now());
        articleToCreate.setUpdatedAt(LocalDateTime.now());

        Article savedArticle = articleRepository.save(articleToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {

        Article oldArticle = articleRepository.findById(id).orElse(null);
        if (oldArticle == null) {
            return ResponseEntity.notFound().build();
        }

        oldArticle.setTitle(articleDetails.getTitle());
        oldArticle.setContent(articleDetails.getContent());
        oldArticle.setUpdatedAt(LocalDateTime.now());

        Article updatedArticle = articleRepository.save(oldArticle);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        Article articleToDelete = articleRepository.findById(id).orElse(null);
        if (articleToDelete==null){
            return ResponseEntity.notFound().build();
        }
        articleRepository.delete((articleToDelete));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search-title")
    public ResponseEntity<List<Article>> getArticlesByTitle(@RequestParam String searchTerms){
        List<Article> articles = articleRepository.findByTitle(searchTerms);
        if(articles.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/contains-world")
    public ResponseEntity<List<Article>> getArticlesByContent(@RequestParam String searchTerms){
        List<Article> articles = articleRepository.findByContentContaining(searchTerms);
        if(articles.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/filter-date")
    public ResponseEntity<List<Article>> getArticlesCreatedAfter(@RequestParam LocalDateTime dateFilter){
        List<Article> articles = articleRepository.findByCreatedAtAfter(dateFilter);
        if(articles.isEmpty()){
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/top2")
    public  ResponseEntity<List<Article>> getLastFiveArticles(){
        List<Article> articles = articleRepository.findFirst2ByOrderByCreatedAt();
        if(articles.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }
}
