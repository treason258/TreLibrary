import {Component} from 'react';

class BaseComponent extends Component {

    componentWillMount() {
        console.log('componentWillMount - ↓↓↓')
        // console.log(this)
    }

    componentDidMount() {
        console.log('componentDidMount - ↓↓↓')
        // console.log(this)
    }

    componentWillUnmount() {
        console.log('componentWillUnmount - ↓↓↓')
        // console.log(this)
    }
}

export default BaseComponent