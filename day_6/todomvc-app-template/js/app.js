let ENTER_KEY_CODE = 13;
let todo_list = [];
$(function () {
	todo_list.concat
	listenEnterPress();

	$('.new-todo').blur(addNewTodo);

	$('.filters a').click(function () {
		$('.filters a').removeClass('selected');
		$(this).addClass('selected');
		updateDisplayTodoList();
		
	});


	$('#toggle-all').change(function () {
		let checked = $(this).prop('checked');
		for (let i = 0; i < todo_list.length; i++) {
			todo_list[i].completed = checked;
			if (checked) {
				$(`.todo-list li[id="${todo_list[i].id}"]`).addClass('completed');
			} else {
				$(`.todo-list li[id="${todo_list[i].id}"]`).removeClass('completed');
			}

			$(`.todo-list li[id="${todo_list[i].id}"]`).attr('completed', todo_list[i].completed);
			$(`.todo-list li[id="${todo_list[i].id}"] :checkbox`).prop('checked', todo_list[i].completed);

		}
		updateDisplayTodoList();
		updateItemLeft();
	});

	$('.clear-completed').click(function () {
		todo_list = todo_list.filter(todo => todo.completed === false);
		$('.todo-list li[completed="true"]').remove();
		updateDisplayTodoList();
    updateItemLeft();
	});

});

function listenEnterPress() {
	$(document).keydown(function (event) {
		if (event.keyCode === ENTER_KEY_CODE) {
			if ($(".new-todo").is(':focus')) {
				addNewTodo();
			} else if ($('.edit').is(':focus')) {
				modifyTodoText('.edit:focus');
			}
		}
	});
}



function addNewTodo() {
	let text = $('.new-todo').val().trim();
	$('.new-todo').val('');
	if (text.length === 0) {
		return;
	}

	let todo = {
		id: new Date().toLocaleString(),
		text,
		completed: false
	}
	todo_list.push(todo);

	let template = $(getTemplate()).attr('id', todo.id).attr('completed', todo.completed).appendTo('.todo-list');

	template.find(':checkbox').change(function () {
		changeTodoState(this);
	});

	template.find('label').text(todo.text).dblclick(function () {
		$(this).parent().parent().addClass('editing');
	});

	template.find('.edit').blur(function () {
		modifyTodoText(this);
	})

	template.find('.destroy').click(function () {
		let todo = $(this).parent().parent();
		let id = todo.attr('id');
		todo.remove();
		todo_list.splice(todo_list.findIndex(todo => todo.id === id), 1);
		updateItemLeft();
	});

	updateDisplayTodoList();
	updateItemLeft();
}

function modifyTodoText(element) {
	let text = $(element).val().trim();
	if (text.length === 0) {
		return;
	}
	let id = $(element).val('').prev().find('label').text(text).parent().parent().removeClass('editing').attr('id');
	todo_list.find(todo => todo.id === id).text = text;
}


function changeTodoState(element) {
	let completed = $(element).prop('checked');
	let id = $(element).parents('li').attr('completed', completed).toggleClass('completed').attr('id');
	todo_list.find(todo => todo.id === id).completed = completed;
	updateDisplayTodoList();
	updateItemLeft();
}


function getTemplate() {
	return `<li><div class="view">
    <input class="toggle" type="checkbox">
    <label></label>
    <button class="destroy"></button>
  </div>
  <input class="edit" value="">
</li>`
}

function updateItemLeft() {
	let count = todo_list.filter(todo => todo.completed === false).length;
	$('.todo-count strong').text(count);
	$('#toggle-all').prop('checked', count === 0);

}


function updateDisplayTodoList() {
	$('.todo-list li').hide();
	switch ($('.filters .selected').text()) {
		case 'All':
			$('.todo-list li').show();
			break;
		case 'Active':
			$('.todo-list li[completed=false]').show();
			break;
		case 'Completed':
			$('.todo-list li[completed=true]').show();
			break;
	}
}
