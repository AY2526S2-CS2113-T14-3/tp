package seedu.duke.tasklist;

import seedu.duke.task.Todo;

public class Category {
    private String name;
    private TodoList todoList;

    public Category(String name) {
        this.name = name;
        this.todoList = new TodoList();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public void addTodo(Todo todo) {
        todoList.add(todo);
    }

    public Todo getTodo(int index) {
        return todoList.get(index);
    }

    public void deleteTodo(int index) {
        todoList.delete(index);
    }

    public void markTodo(int index) {
        todoList.mark(index);
    }

    public void unmarkTodo(int index) {
        todoList.unmark(index);
    }

    public void setTodoPriority(int index, int priority) {
        todoList.setPriority(index, priority);
    }

    public void reorderTodo(int fromIndex, int toIndex) {
        todoList.reorder(fromIndex, toIndex);
    }

    public String toString() {
        String result = "";
        result += "---" + getName() + "---" + System.lineSeparator();
        result += todoList.toString();
        return result;
    }
}