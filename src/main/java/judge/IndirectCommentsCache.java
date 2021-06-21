package judge;

import judge.dataTransferObject.Problem;
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
    public static void addIfAbsent(Problem problem, Comment parent, LinkedList<Comment> indirectComments)
    {
        addIfAbsent(problem.getId(), parent.getId(), indirectComments);
    }
    public static void addIfAbsent(int problemId, int parentId, LinkedList<Comment> indirectComments)
    {
        cache.putIfAbsent(problemId, new HashMap<Integer, LinkedList<Comment>>());
        cache.get(problemId).putIfAbsent(parentId, indirectComments);
    }
    public static boolean contains(Problem problem, Comment parent)
    {
        return contains(problem.getId(), parent.getId());
    }
    public static boolean contains(int problemId, int parentId)
    {
        HashMap<Integer, LinkedList<Comment>> value = cache.get(problemId);
        return value != null && value.containsKey(parentId);
    }
    public static List<Comment> get(Problem problem, Comment parent)
    {
        return get(problem.getId(), parent.getId());
    }
    public static List<Comment> get(int problemId, int parentId)
    {
        HashMap<Integer, LinkedList<Comment>> value = cache.get(problemId);
        return value == null ? null : value.get(parentId);
    }
    public static List<Comment> remove(Problem problem, Comment parent)
    {
        return remove(problem.getId(), parent.getId());
    }
    public static List<Comment> remove(int problemId, int parentId)
    {
        HashMap<Integer, LinkedList<Comment>> value = cache.get(problemId);
        return value == null ? null : value.remove(parentId);
    }
    public static void set(Problem problem, Comment parent, LinkedList<Comment> indirectComments)
    {
        set(problem.getId(), parent.getId(), indirectComments);
    }
    public static void set(int problemId, int parentId, LinkedList<Comment> indirectComments)
    {
        cache.putIfAbsent(problemId, new HashMap<Integer, LinkedList<Comment>>());
        cache.get(problemId).put(parentId, indirectComments);
    }
}
