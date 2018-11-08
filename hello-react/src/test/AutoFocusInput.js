import React, {Component} from 'react'

class AutoFocusInput extends Component {
    constructor() {
        super();
    }

    componentDidMount() {
        this.input.focus()
    }

    render() {
        return (
            <div>
                <input ref={(input) => this.input = input}/>
            </div>
        )
    }
}

export default AutoFocusInput