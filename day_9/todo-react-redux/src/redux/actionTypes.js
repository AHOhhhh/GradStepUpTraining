export const addTodo='ADD_TODO';
export const deleteTodo='DELETE_TODO';
export const modifyTodoState='MODIFY_TODO_STATE';
export const modifyTodoText='COMPLETE_TODO_TEXT';
export const modifyAllTodoState='MODIFY_ALL_TODO_STATE';
export const deleteAllCompletedTodos='DELETE_ALL_COMPLETED_TODOS';
export const modifyDisplayMode='MODIFY_DISPLAY_MODE';
export const formatAction=(type,data)=>({type,data});