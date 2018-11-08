import React, {Component} from 'react';
import Header from './Header.js';
import Clock from "./Clock";

class All extends Component {
    constructor() {
        super()
        this.state = {
            isShow: true
        }
    }

    handleShowOrHide () {
        this.setState({
            isShow: !this.state.isShow
        })
    }

    render() {
        return (
            <div>
                <button onClick={this.handleShowOrHide.bind(this)}>
                    显示或者隐藏标题
                </button>
                {this.state.isShow ? <Clock /> : null}
            </div>
        )
    }
}

export default All;