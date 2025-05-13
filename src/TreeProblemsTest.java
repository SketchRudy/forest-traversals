import static org.junit.Assert.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeProblemsTest {
  private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private PrintStream testOut;

  @Before
  public void setUpStreams() {
    testOut = new PrintStream(new TeeOutputStream(originalOut, baos));
    System.setOut(testOut);
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  // ============================
  // postOrder (Node version) tests
  // ============================

  @Test
  public void testPostOrderNode_NullTree() {
    TreeProblems.postOrder(null);
    assertEquals("A null Node tree should produce no output", "", baos.toString());
  }

  @Test
  public void testPostOrderNode_SingleNode() {
    Node<Integer> single = new Node<>(42);
    TreeProblems.postOrder(single);
    assertEquals("A single-node tree should print its value", "42\r\n", baos.toString());
  }

  @Test
  public void testPostOrderNode_ComplexTree() {
    Node<Integer> root = new Node<>(5);
    Node<Integer> node3 = new Node<>(3);
    Node<Integer> node9 = new Node<>(9);
    Node<Integer> node8 = new Node<>(8);
    root.children.add(node3);
    root.children.add(node9);
    root.children.add(node8);
    Node<Integer> node4 = new Node<>(4);
    Node<Integer> node1 = new Node<>(1);
    Node<Integer> node2 = new Node<>(2);
    node9.children.add(node4);
    node9.children.add(node1);
    node9.children.add(node2);

    TreeProblems.postOrder(root);
    assertEquals("Complex Node tree postorder traversal output is incorrect", "3\r\n4\r\n1\r\n2\r\n9\r\n8\r\n5\r\n", baos.toString());
  }

  // ============================
  // postOrder (Map version) tests
  // ============================

  @Test
  public void testPostOrderMap_NullTree() {
    TreeProblems.postOrder(null, 0);
    assertEquals("A null Map tree should produce no output", "", baos.toString());
  }

  @Test
  public void testPostOrderMap_RootNotInTree() {
    Map<Integer, List<Integer>> tree = new HashMap<>();
    tree.put(1, List.of(2, 3));
    tree.put(2, List.of());
    tree.put(3, List.of());
    TreeProblems.postOrder(tree, 99);
    assertEquals("A tree that does not contain the root should produce no output", "", baos.toString());
  }

  @Test
  public void testPostOrderMap_SingleNode() {
    Map<Integer, List<Integer>> tree = new HashMap<>();
    tree.put(42, List.of());
    TreeProblems.postOrder(tree, 42);
    assertEquals("A single-node Map tree should print its value", "42\r\n", baos.toString());
  }

  @Test
  public void testPostOrderMap_ComplexTree() {
    Map<Integer, List<Integer>> tree = new HashMap<>();
    tree.put(5, List.of(3, 9, 8));
    tree.put(3, List.of());
    tree.put(8, List.of());
    tree.put(9, List.of(4, 1, 2));
    tree.put(4, List.of());
    tree.put(1, List.of());
    tree.put(2, List.of());

    TreeProblems.postOrder(tree, 5);
    assertEquals("Complex Map tree postorder traversal output is incorrect", "3\r\n4\r\n1\r\n2\r\n9\r\n8\r\n5\r\n", baos.toString());
  }

  // =============================
  // Helper methods for Node trees
  // =============================

  /**
   * Builds an integer tree (using Node<Integer>) with non-sorted node values.
   * Structure:
   * 
   *             7
   *          /  |  \
   *        11   3    2
   *            / \
   *           5  -1
   *                \
   *                 9
   * 
   */
  private Node<Integer> buildMixedIntegerTree() {
    Node<Integer> root = new Node<>(7);
    // Adding children in a mixed order
    Node<Integer> child1 = new Node<>(11);
    Node<Integer> child2 = new Node<>(3);
    Node<Integer> child3 = new Node<>(2);
    root.children.add(child1);
    root.children.add(child2);
    root.children.add(child3);

    // For node 3, add children (order is intentionally jumbled)
    Node<Integer> child2a = new Node<>(5);
    Node<Integer> child2b = new Node<>(-1);
    child2.children.add(child2b); // intentionally add -1 first
    child2.children.add(child2a);

    // For node -1, add one child
    Node<Integer> child2b_child = new Node<>(9);
    child2b.children.add(child2b_child);

    return root;
  }

  // ============================
  // sumTree (Node version) tests
  // ============================

  @Test
  public void testSumTreeNode_NullTree() {
    Node<Integer> nullNode = null;
    assertEquals("A null Node tree should yield 0", 0, TreeProblems.sumTree(nullNode));
  }

  @Test
  public void testSumTreeNode_SingleNode() {
    Node<Integer> single = new Node<>(42);
    assertEquals("A single-node tree should return its value", 42, TreeProblems.sumTree(single));
  }

  @Test
  public void testSumTreeNode_ComplexTree() {
    Node<Integer> root = buildMixedIntegerTree();
    assertEquals("Sum of nodes in a complex Node tree is incorrect", 36, TreeProblems.sumTree(root));
  }

  // ============================
  // sumTree (Map version) tests
  // ============================

  @Test
  public void testSumTreeMap_NullTree() {
    Map<Integer, List<Integer>> nullTree = null;
    assertEquals("A null Map tree should yield 0", 0, TreeProblems.sumTree(nullTree));
  }

  @Test
  public void testSumTreeMap_SingleNode() {
    Map<Integer, List<Integer>> tree = new HashMap<>();
    tree.put(42, List.of());
    assertEquals("A Map with a single key should return that key's value", 42, TreeProblems.sumTree(tree));
  }

  @Test
  public void testSumTreeMap_ComplexTree() {
    Map<Integer, List<Integer>> tree = new HashMap<>();
    tree.put(7, List.of(2, 11, 3));
    tree.put(3, List.of(-1, 5));
    tree.put(11, List.of());
    tree.put(2, List.of(8));
    tree.put(-1, List.of(9));
    tree.put(5, List.of());
    tree.put(8, List.of());
    tree.put(9, List.of());

    assertEquals("Sum of nodes in a complex Map tree is incorrect", 44, TreeProblems.sumTree(tree));
  }

  // ============================
  // findRoot tests
  // ============================

  @Test
  public void testFindRoot_SingleNode() {
    Map<Integer, List<Integer>> tree = new HashMap<>();
    tree.put(42, List.of());
    assertEquals("Single-node tree: root should be 42", 42, TreeProblems.findRoot(tree).intValue());
  }

  @Test
  public void testFindRoot_ProvidedExample() {
    Map<Integer, List<Integer>> tree = new HashMap<>();
    tree.put(20, List.of(40));
    tree.put(8, List.of());
    tree.put(30, List.of());
    tree.put(10, List.of(20, 30, 99));
    tree.put(40, List.of());
    tree.put(99, List.of(8));

    assertEquals("findRoot did not return the correct root for the provided example", 10, TreeProblems.findRoot(tree).intValue());
  }

  @Test
  public void testFindRoot_RandomOrder() {
    Map<Integer, List<Integer>> tree = new HashMap<>();
    tree.put(13, List.of(19, 7));
    tree.put(7, List.of(3));
    tree.put(19, List.of());
    tree.put(3, List.of(2, 11));
    tree.put(2, List.of());
    tree.put(11, List.of());

    assertEquals("findRoot did not identify the correct root in a mixed-order tree", 13, TreeProblems.findRoot(tree).intValue());
  }

  // ============================
  // maxDepth (Node version) tests
  // ============================

  @Test
  public void testMaxDepthNode_NullTree() {
    Node<Integer> nullNode = null;
    assertEquals("A null Node tree should have depth 0", 0, TreeProblems.maxDepth(nullNode));
  }

  @Test
  public void testMaxDepthNode_SingleNode() {
    Node<String> single = new Node<>("solo");
    assertEquals("A single-node tree should have depth 1", 1, TreeProblems.maxDepth(single));
  }

  @Test
  public void testMaxDepthNode_ComplexTree() {
    assertEquals("Tree with 4 levels should have a depth of 4", 4, TreeProblems.maxDepth(buildMixedIntegerTree()));
  }

  // ============================
  // maxDepth (Map version) tests
  // ============================

  @Test
  public void testMaxDepthMap_NullTree() {
    Map<String, List<String>> nullTree = null;
    assertEquals("A null Map tree should have depth 0", 0, TreeProblems.maxDepth(nullTree));
  }

  @Test
  public void testMaxDepthMap_SingleNode() {
    Map<String, List<String>> tree = new HashMap<>();
    tree.put("solo", List.of());
    assertEquals("A single-node Map tree should have depth 1", 1, TreeProblems.maxDepth(tree));
  }

  @Test
  public void testMaxDepthMap_ComplexTree() {
    Map<String, List<String>> tree = new HashMap<>();
    tree.put("t2", List.of("t1", "t3", "2child"));
    tree.put("t1", List.of("t4"));
    tree.put("t3", List.of());
    tree.put("2child", List.of());
    tree.put("t4", List.of("t6"));
    tree.put("t6", List.of());

    assertEquals("maxDepth (Map) did not compute the correct depth", 4, TreeProblems.maxDepth(tree));
  }

  // ====================================================
  // TeeOutputStream inner class for capturing output
  // ====================================================
  // Used for testing purposes so you can still see your print statements when debugging
  // You do not need to modify this 
  static class TeeOutputStream extends OutputStream {
    private final OutputStream first;
    private final OutputStream second;

    public TeeOutputStream(OutputStream first, OutputStream second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public void write(int b) {
      try {
        first.write(b);
        second.write(b);
      } catch (Exception e) {
        throw new RuntimeException("Error writing to TeeOutputStream", e);
      }
    }

    @Override
    public void flush() {
      try {
        first.flush();
        second.flush();
      } catch (Exception e) {
        throw new RuntimeException("Error flushing TeeOutputStream", e);
      }
    }

    @Override
    public void close() {
      try {
        first.close();
        second.close();
      } catch (Exception e) {
        throw new RuntimeException("Error closing TeeOutputStream", e);
      }
    }
  }

  @org.junit.jupiter.api.Test
  void testFindRoot() {
    
  }

  @org.junit.jupiter.api.Test
  void testMaxDepth() {
    
  }

  @org.junit.jupiter.api.Test
  void testMaxDepth2() {
    
  }

  @org.junit.jupiter.api.Test
  void testPostOrder() {
    
  }

  @org.junit.jupiter.api.Test
  void testPostOrder2() {
    
  }

  @org.junit.jupiter.api.Test
  void testSumTree() {
    
  }

  @org.junit.jupiter.api.Test
  void testSumTree2() {
    
  }
}
