import React from 'react'
import Input from 'antd/lib/input'

const EditableCell = ({editable, value, onChange, onClicked, onBlur}) => {
  return (
    <div>
      {editable
        ? <Input value={value} onChange={e => onChange(e.target.value)} onBlur={onBlur} onClick={() => onClicked()}/>
        : <p onClick={() => onClicked()}>{value}</p>
      }
    </div>
  )
}

export default EditableCell

