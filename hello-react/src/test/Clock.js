import React, {Component} from 'react'

class Clock extends Component {

    constructor() {
        console.log('constructor')
        super()
        this.state = {
            date: new Date()
        }
    }

    // shouldComponentUpdate(nextProps, nextState) {
    //     console.log('shouldComponentUpdate')
    // }

    // componentWillReceiveProps(nextProps) {
    //     console.log('componentWillReceiveProps')
    // }

    componentWillMount() {
        console.log('componentWillMount')
        this.timer = setInterval(() => {
            this.setState({date: new Date()})
        })
    }

    componentDidMount() {
        console.log('componentDidMount')
    }

    componentWillUnmount() {
        console.log('componentWillUnmount')
        clearInterval(this.timer)
    }

    render() {
        console.log('render')
        return (
            <div>
                <h2>
                    <p>现在的时间是</p>
                    {this.state.date.toLocaleTimeString()}
                </h2>
            </div>
        )
    }
}

export default Clock