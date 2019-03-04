import React, {Component, PropTypes} from 'react' // eslint-disable-line
import Step from './Step'

class SignSteppers extends Component { // eslint-disable-line

  static propTypes = {
    current: PropTypes.number.isRequired
  }

  render() {
    const itemWidth = (100 / this.props.children.length) + '%'
    const {current} = this.props
    return (
      <div>
        {this.props.children.map((Item, index) => {
          let status = 'wait'
          if (index < current) {
            status = 'finish'
          } else if (index === current) {
            status = 'process'
          }
          return React.cloneElement(Item, {
            key: Item.props.title,
            width: itemWidth,
            status
          })
        })}
      </div>
    )
  }
}

SignSteppers.Step = Step

export default SignSteppers
