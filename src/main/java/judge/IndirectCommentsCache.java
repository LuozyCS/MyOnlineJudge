package judge;

import judge.dataTransferObject.Article;
import judge.dataTransferObject.Comment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Component public class IndirectCommentsCache
{
    private static HashMap<Integer, HashMap<Integer, LinkedList<Comment>>> cache
            = new HashMap<Integer, HashMap<Integer, LinkedList<Comment>>>(5);
    private IndirectCommentsCache()
    {
    }
    public static void addIfAbsent(Article article, Comment parent, LinkedList<Comment> indirectComments)
    {
        addIfAbsent(article.getId(), parent.getId(), indirectComments);
    }
    public static void addIfAbsent(int articleId, int parentId, LinkedList<Comment> indirectComments)
    {
        cache.putIfAbsent(articleId, new HashMap<Integer, LinkedList<Comment>>());
        cache.get(articleId).putIfAbsent(parentId, indirectComments);
    }
    public static boolean contains(Article article, Comment parent)
    {
        return contains(article.getId(), parent.getId());
    }
    public static boolean contains(int articleId, int parentId)
    {
        HashMap<Integer, LinkedList<Comment>> value = cache.get(articleId);
        return value != null && value.containsKey(parentId);
    }
    public static List<Comment> get(Article article, Comment parent)
    {
        return get(article.getId(), parent.getId());
    }
    public static List<Comment> get(int articleId, int parentId)
    {
        HashMap<Integer, LinkedList<Comment>> value = cache.get(articleId);
        return value == null ? null : value.get(parentId);
    }
    public static List<Comment> remove(Article article, Comment parent)
    {
        return remove(article.getId(), parent.getId());
    }
    public static List<Comment> remove(int articleId, int parentId)
    {
        HashMap<Integer, LinkedList<Comment>> value = cache.get(articleId);
        return value == null ? null : value.remove(parentId);
    }
    public static void set(Article article, Comment parent, LinkedList<Comment> indirectComments)
    {
        set(article.getId(), parent.getId(), indirectComments);
    }
    public static void set(int articleId, int parentId, LinkedList<Comment> indirectComments)
    {
        cache.putIfAbsent(articleId, new HashMap<Integer, LinkedList<Comment>>());
        cache.get(articleId).put(parentId, indirectComments);
    }
}
