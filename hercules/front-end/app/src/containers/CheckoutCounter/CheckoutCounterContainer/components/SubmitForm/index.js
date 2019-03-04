import React, {Component, PropTypes} from 'react';

export default class SubmitForm extends Component {
  static propTypes = {
    submitInfo: PropTypes.object
  }

  submit() {
    this.form.submit()
  }

  renderFormContent() {
    const redirectBody = this.props.submitInfo.redirectBody
    return Object.keys(redirectBody).map((item) => {
      return (<input type="hidden" name={item} value={redirectBody[item] || ''}/>)
    })
  }


  render() {
    return (
      <form
        action="https://gateway.hnapay.com/website/pay.htm"
        method="post"
        name="orderForm"
        target="newPayment"
        ref={(ref) => {
          this.form = ref
        }}
      >{this.renderFormContent()}</form>
    )
  }
}
