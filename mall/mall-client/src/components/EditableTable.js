import React, { Component } from 'react';
import { Table, Button, Popconfirm, Form, Input } from 'antd';

const FormItem = Form.Item;
const EditableContext = React.createContext();

const EditableRow = ({ form, index, ...props }) => (
  <EditableContext.Provider value={form}>
    <tr  {...props} />
  </EditableContext.Provider>
);

const EditableFormRow = Form.create()(EditableRow);


class EditableCell extends Component {
  state = {
    editing: false,
  }

  componentDidMount() {
    if (this.props.editable) {
      document.addEventListener('click', this.handleClickOutside, true);
    }
  }

  componentWillUnmount() {
    if (this.props.editable) {
      document.removeEventListener('click', this.handleClickOutside, true);
    }
  }

  toggleEdit = () => {
    const editing = !this.state.editing;
    this.setState({ editing }, () => {
      if (editing) {
        this.input.focus();
      }
    });
  }

  handleClickOutside = (e) => {
    const { editing } = this.state;
    if (editing && this.cell !== e.target && !this.cell.contains(e.target)) {
      this.save();
    }
  }

  update = () => {
    const { record, updataOrderItemHandle } = this.props;
    this.form.validateFields((error, values) => {
      if (error) {
        return;
      }
      this.toggleEdit();
      let count = parseInt(values.count);
      if (count > 0) {
        updataOrderItemHandle(record.key, count);
      }
    });
  }

  render() {
    const { editing } = this.state;
    const { editable, dataIndex, title, record, index, updataOrderItemHandle, ...restProps } = this.props;
    return (
      <td ref={node => (this.cell = node)} {...restProps}>
        {editable ? (
          <EditableContext.Consumer>
            {(form) => {
              this.form = form;
              return (
                editing ? (
                  <FormItem style={{ margin: 0 }}>
                    {form.getFieldDecorator(dataIndex, {
                      rules: [{
                        required: true,
                        message: `商品数量不能为空`,
                      }],
                      initialValue: record[dataIndex],
                    })(
                      <Input min={1}
                        type="number"
                        ref={node => (this.input = node)}
                        onPressEnter={this.update}
                      />
                    )}
                  </FormItem>
                ) : (
                    <div
                      className="editable-cell-value-wrap"
                      onClick={this.toggleEdit}>
                      {restProps.children}
                    </div>
                  )
              );
            }}
          </EditableContext.Consumer>
        ) : restProps.children}
      </td>
    );
  }
}

class EditableTable extends Component {
  constructor(props) {
    super(props);
    this.columns = [
      { title: "名字", dataIndex: "name", key: "name", width: "20%" },
      { title: "单价", dataIndex: "price", key: "price", width: "20%" },
      { title: "数量", dataIndex: "count", key: "count", width: "20%", editable: true },
      { title: "单位", dataIndex: "unit", key: "unit", width: "20%" },
      {
        title: "操作", dataIndex: "", key: "action", width: "10%", render: (text, record) => {
          return (<Popconfirm title="你确定要删除该商品吗？" onConfirm={() => this.handleDelete(record.key)}>
            <Button type="danger">删除</Button>
          </Popconfirm>);
        }
      }];
  }


  handleDelete = (key) => {
    let { deleteOrderItemHandle } = this.props;
    deleteOrderItemHandle(key);
  }



  render() {
    const { dataSource, updataOrderItemHandle } = this.props;
    const components = {
      body: {
        row: EditableFormRow,
        cell: EditableCell,
      },
    };
    const columns = this.columns.map((col) => {
      if (!col.editable) {
        return col;
      }
      return {
        ...col,
        onCell: record => ({
          record,
          editable: col.editable,
          dataIndex: col.dataIndex,
          title: col.title,
          updataOrderItemHandle: updataOrderItemHandle,
        }),
      };
    });
    return (
      <Table
        components={components}
        rowClassName={() => 'editable-row'}
        pagination={false}
        dataSource={dataSource}
        columns={columns}
      />
    );
  }
}
export default EditableTable;