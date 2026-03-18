package seedu.duke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.duke.exception.UniTaskerException;
import seedu.duke.tasklist.CategoryList;

class DukeTest {
    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    public void addCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        assertEquals(1, categoryList.getAmount());
        assertEquals("School", categoryList.getCategory(0).getName());
    }

    @Test
    public void deleteCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addCategory("Work");

        categoryList.deleteCategory(0);

        assertEquals(1, categoryList.getAmount());
        assertEquals("Work", categoryList.getCategory(0).getName());
    }

    @Test
    public void addTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");

        categoryList.addTodo(0, "finish tutorial");

        assertEquals(1, categoryList.getCategory(0).getTodoList().getSize());
        assertEquals("finish tutorial", categoryList.getCategory(0).getTodo(0).getDescription());
    }

    @Test
    public void deleteTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "task 1");
        categoryList.addTodo(0, "task 2");

        categoryList.deleteTodo(0, 0);

        assertEquals(1, categoryList.getCategory(0).getTodoList().getSize());
        assertEquals("task 2", categoryList.getCategory(0).getTodo(0).getDescription());
    }

    @Test
    public void markTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "task 1");

        try {
            categoryList.markTodo(0, 0);
        } catch (UniTaskerException e) {
            //ignore
        }

        assertEquals(true, categoryList.getCategory(0).getTodo(0).getIsDone());
    }

    @Test
    public void unmarkTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "task 1");
        try {
            categoryList.markTodo(0, 0);
            categoryList.unmarkTodo(0, 0);
        } catch (UniTaskerException e) {
            //ignore
        }

        assertEquals(false, categoryList.getCategory(0).getTodo(0).getIsDone());
    }

    @Test
    public void reorderCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addCategory("Work");

        try {
            categoryList.reorderCategory(0, 1);
        } catch (UniTaskerException e) {
            //ignore
        }

        assertEquals("Work", categoryList.getCategory(0).getName());
        assertEquals("School", categoryList.getCategory(1).getName());
    }

    @Test
    public void reorderTodo_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "task 1");
        categoryList.addTodo(0, "task 2");

        try {
            categoryList.reorderTodo(0, 0, 1);
        } catch (UniTaskerException e) {
            //ignore
        }

        assertEquals("task 2", categoryList.getCategory(0).getTodo(0).getDescription());
        assertEquals("task 1", categoryList.getCategory(0).getTodo(1).getDescription());
    }

    @Test
    public void setTodoPriority_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "finish tutorial");

        try {
            categoryList.setTodoPriority(0, 0, 3);
        } catch (UniTaskerException e) {
            // ignore
        }

        assertEquals(3, categoryList.getCategory(0).getTodo(0).getPriority());
    }

    @Test
    public void sortTodoByPriority_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "low priority task");
        categoryList.addTodo(0, "high priority task");
        categoryList.addTodo(0, "medium priority task");

        try {
            categoryList.setTodoPriority(0, 0, 1); // low
            categoryList.setTodoPriority(0, 1, 5); // high
            categoryList.setTodoPriority(0, 2, 3); // medium
        } catch (UniTaskerException e) {
            // ignore
        }

        categoryList.getCategory(0).getTodoList().sortByPriority();

        assertEquals("high priority task", categoryList.getCategory(0).getTodo(0).getDescription());
        assertEquals("medium priority task", categoryList.getCategory(0).getTodo(1).getDescription());
        assertEquals("low priority task", categoryList.getCategory(0).getTodo(2).getDescription());
    }

    @Test
    public void listCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addCategory("Work");

        String output = categoryList.toString();

        assertTrue(output.contains("School"));
        assertTrue(output.contains("Work"));
    }

    @Test
    public void listSingleCategory_success() {
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory("School");
        categoryList.addTodo(0, "finish tutorial");

        String output = categoryList.getCategory(0).toString();

        assertTrue(output.contains("School"));
        assertTrue(output.contains("finish tutorial"));
    }
}

