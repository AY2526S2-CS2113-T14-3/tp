package seedu.duke.tasklist;

import java.util.ArrayList;

import seedu.duke.task.Todo;

public class CategoryList {
    private ArrayList<Category> categories;

    public CategoryList() {
        categories = new ArrayList<>();
    }

    public void addCategory(String name) {
        categories.add(new Category(name));
    }

    public void addTodo(int categoryIndex, String description) {
        categories.get(categoryIndex).addTodo(new Todo(description));
    }

    public void deleteCategory(int index) {
        categories.remove(index);
    }

    public void deleteTodo(int categoryIndex, int todoIndex) {
        categories.get(categoryIndex).deleteTodo(todoIndex);
    }

    public void markTodo(int categoryIndex, int todoIndex) {
        categories.get(categoryIndex).markTodo(todoIndex);
    }

    public void unmarkTodo(int categoryIndex, int todoIndex) {
        categories.get(categoryIndex).unmarkTodo(todoIndex);
    }

    public void reorderCategory(int categoryIndex1, int categoryIndex2) {
        Category category = categories.remove(categoryIndex1);
        categories.add(categoryIndex2, category);
    }

    public void reorderTodo(int categoryIndex, int todoIndex1, int todoIndex2) {
        categories.get(categoryIndex).reorderTodo(todoIndex1, todoIndex2);
    }

    public void setTodoPriority(int categoryIndex, int todoIndex, int priority) {
        categories.get(categoryIndex).setTodoPriority(todoIndex, priority);
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < categories.size(); i += 1) {
            result += "[" + (i + 1) + "]" + categories.get(i).toString();
        }
        return result;
    }
}
