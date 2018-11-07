import React, {Component} from 'react';
import Header from './Header.js';

class All extends Component {
    constructor() {
        super()
        this.state = {
            isShowHeader: true
        }
    }

    handleShowOrHide () {
        this.setState({
            isShowHeader: !this.state.isShowHeader
        })
    }

    render() {
        return (
            <div>
                {this.state.isShowHeader ? <Header /> : null}
                <button onClick={this.handleShowOrHide.bind(this)}>
                    显示或者隐藏标题
                </button>
            </div>
        )
    }
}

export default All;