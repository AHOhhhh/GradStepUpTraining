class TodoContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = { todos:[] ,display:'all'};
    this.addTodo=this.addTodo.bind(this);
    this.changeTodoState=this.changeTodoState.bind(this);
    this.deleteTodo=this.deleteTodo.bind(this);
    this.toggleAllTodos=this.toggleAllTodos.bind(this);
    this.deleleAllCompleteTodos=this.deleleAllCompleteTodos.bind(this);
    this.changeDisplayMode=this.changeDisplayMode.bind(this);
    this.changeTodoText=this.changeTodoText.bind(this);
  }

  addTodo(todo){
    if(todo.text===''){
      return
    }
    this.setState({todos: this.state.todos.concat(todo)});
  }
  changeTodoState(id,completed){
    this.state.todos.find(item=>item.id===id).completed=completed;
    this.setState(this.state);
  }
  changeTodoText(id,text){
    this.state.todos.find(item=>item.id===id).text=text;
    this.setState(this.state);
  }
  deleteTodo(id){
    this.state.todos.splice(this.state.todos.findIndex(todo=>todo.id===id),1);
    this.setState(this.state);			
  }
  deleleAllCompleteTodos(){
    this.state.todos=  this.state.todos.filter(item=>!item.completed)
    this.setState(this.state);
  }
  toggleAllTodos(completed){
    this.state.todos.forEach(todo => {
      todo.completed=completed;
    });
    this.setState(this.state);
  }
  changeDisplayMode(mode){
  this.setState({display:mode})
  }
  render() {
    return (
      <section className="todoapp">
        <TodoHeader addTodo={this.addTodo} />
        <TodoMain todos={this.state.todos}  mode={this.state.display} changeState={this.changeTodoState} toggleAll={this.toggleAllTodos} deleteTodo={this.deleteTodo} changeTodoText={this.changeTodoText}/>
        <TodoFooter itemCount={this.state.todos.filter(item=>!item.completed).length} deleleAll={this.deleleAllCompleteTodos} mode={this.state.display} changeMode={this.changeDisplayMode}/>
      </section>
    );
  }
}