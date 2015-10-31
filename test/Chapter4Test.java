package chapter4;

import static debug.Debugger.DEBUG;
import static org.hamcrest.Matcher.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsNull.*;


import java.lang.*;
import java.util.*;
import java.util.stream.*;

import org.hamcrest.core.*;
import org.junit.*;
import org.junit.rules.*;
import structures.*;
import test.*;

public class Chapter4Test extends ChapterTestBase {

  BinaryTreeBalanceChecker balanceChecker = new BinaryTreeBalanceChecker();
  TreeFromSortedNumbersCreator treeCreator = new TreeFromSortedNumbersCreator();
  ListsFromTreeLevelsCreator listsFromLevels = new ListsFromTreeLevelsCreator();


  @Test
  public void testBinaryTreeBalanceChecker() {
    // given
    BinaryTreeNode<String> node = node("A");
    node.left(node("B"));
    node.right(node("C"));
    node.left().left(node("D"));
    node.left().left().left(node("H"));
    node.left().left().right(node("I"));
    node.left().right(node("E"));
    node.right().left(node("F"));
    node.right().right(node("G"));


    // when
    boolean actual = balanceChecker.check(node);

    // then
    areEqual(actual, true);

    // when I make the tree imbalanced
    node.left().left().right().right(node("J"));
    // then
    areEqual(balanceChecker.check(node), false);
  }

  private static <T> BinaryTreeNode<T> node(T value) {
    return new BinaryTreeNode<T>(value);
  }

  @Test
  public void testPathChecker() {
    // given
    DirectedNode x = dirNode("X");
    DirectedNode y = dirNode("Y");
    DirectedNode z = dirNode("Z");

    DirectedNode a = dirNode("A");
    DirectedNode b = dirNode("B");
    DirectedNode c = dirNode("C");
    DirectedNode d = dirNode("D");
    DirectedNode e = dirNode("E");
    DirectedNode f = dirNode("F");
    DirectedNode g = dirNode("G");
    DirectedNode h = dirNode("H");
    DirectedNode i = dirNode("I");
    DirectedNode j = dirNode("J");
    DirectedNode k = dirNode("K");
    DirectedNode l = dirNode("L");

    linkTo(x, b, a);
    linkTo(y, i, h, g);
    linkTo(z, k, l);

    linkTo(a, b, c);
    linkTo(b, c, d);
    linkTo(c, e, d);
    linkTo(d, e, x);
    linkTo(e, h, i);
    linkTo(f, d, e);
    linkTo(g, f, e);
    linkTo(j, d, f, k, l);
    linkTo(l, k);

    // then when
    PathChecker checker = new PathChecker();
    areEqual(checker.pathExists(x, y), true);
    areEqual(checker.pathExists(y, x), true);

    areEqual(checker.pathExists(y, z), false);
    areEqual(checker.pathExists(z, y), false);

    areEqual(checker.pathExists(z, x), false);
    areEqual(checker.pathExists(x, z), false);
  }

  private static <T> DirectedNode<T> dirNode(T value) {
    return new DirectedNode(value);
  }

  private static void linkTo(DirectedNode source, DirectedNode ... destinations) {
    for(DirectedNode destination : destinations) {
      source.linkTo(destination);
    }
  }

  @Test
  public void testTreeFromSortedNumbersCreator() {
    // given
    BinaryTreeNode<Integer> five = node(5);
    BinaryTreeNode<Integer> three = node(3);
    BinaryTreeNode<Integer> seven = node(7);
    BinaryTreeNode<Integer> two = node(2);
    BinaryTreeNode<Integer> four = node(4);
    BinaryTreeNode<Integer> six = node(6);
    BinaryTreeNode<Integer> eight = node(8);
    BinaryTreeNode<Integer> one = node(1);
    BinaryTreeNode<Integer> nine = node(9);

    // level 0
    five.left(three);five.right(eight);
    // level 1
    three.left(two);three.right(four);
    eight.left(seven);eight.right(nine);
    // level 2
    two.left(one);
    seven.left(six);

    int[] numbers = { 1,2,3,4,5,6,7,8,9 };

    // when
    BinaryTreeNode<Integer> tree = treeCreator.create(numbers);
    // then
    areEqual(tree, five);
    areEqual(balanceChecker.check(tree), true);
  }

  @Test
  public void testTreeFromSortedNumbersCreator_cases() {
    checkTreeFromSortedNumbersCreator(1);
    checkTreeFromSortedNumbersCreator(1, 2);
    checkTreeFromSortedNumbersCreator(1, 2, 3);
    checkTreeFromSortedNumbersCreator(1, 2, 3, 4);
    checkTreeFromSortedNumbersCreator(1, 2, 3, 4, 5);
    checkTreeFromSortedNumbersCreator(1, 2, 3, 4, 5, 6);
  }

  private void checkTreeFromSortedNumbersCreator(int ... numbers) {
    // when
    BinaryTreeNode<Integer> tree = treeCreator.create(numbers);
    // then
    areEqual(balanceChecker.check(tree), true);
  }

  @Test
  public void testCreateListsFromTreeLevels() {
    // given
    BinaryTreeNode<Integer> five = node(5);
    BinaryTreeNode<Integer> three = node(3);
    BinaryTreeNode<Integer> seven = node(7);
    BinaryTreeNode<Integer> two = node(2);
    BinaryTreeNode<Integer> four = node(4);
    BinaryTreeNode<Integer> six = node(6);
    BinaryTreeNode<Integer> eight = node(8);
    BinaryTreeNode<Integer> one = node(1);
    BinaryTreeNode<Integer> nine = node(9);

    // level 0
    five.left(three);five.right(eight);
    // level 1
    three.left(two);three.right(four);
    eight.left(seven);eight.right(nine);
    // level 2
    two.left(one);
    seven.left(six);

    // when
    structures.LinkedList<structures.LinkedList> result = listsFromLevels.create(five);

    // then
    DEBUG.println(result.toString());
    areEqual(result.get(0).contains(five.value()), true);
    areEqual(result.get(0).size(), 1);

    areEqual(result.get(1).contains(eight.value()), true);
    areEqual(result.get(1).contains(three.value()), true);
    areEqual(result.get(1).size(), 2);


    areEqual(result.get(2).contains(four.value()), true);
    areEqual(result.get(2).contains(two.value()), true);
    areEqual(result.get(2).contains(nine.value()), true);
    areEqual(result.get(2).contains(seven.value()), true);
    areEqual(result.get(2).size(), 4);

    areEqual(result.get(3).contains(six.value()), true);
    areEqual(result.get(3).contains(one.value()), true);
    areEqual(result.get(3).size(), 2);
  }
}
