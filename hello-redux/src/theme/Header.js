import React, {Component} from 'react'
import PropTypes from 'prop-types'
import {connect} from './react-redux'

class Header extends Component {

    // static contextTypes = {
    //     store: PropTypes.object
    // }

    static propTypes = {
        themeColor: PropTypes.string
    }

    // constructor() {
    //     super()
    //     this.state = {themeColor: ''}
    // }
    //
    // componentWillMount() {
    //     this._updateThemeColor()
    //     const {store} = this.context
    //     store.subscribe(() => this._updateThemeColor())
    // }
    //
    // _updateThemeColor() {
    //     const {store} = this.context
    //     const state = store.getState()
    //     this.setState({themeColor: state.themeColor})
    // }

    render() {
        return (
            <h1 style={{color: this.props.themeColor}}>React.js 小书</h1>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        themeColor: state.themeColor
    }
}
Header = connect(mapStateToProps)(Header)

export default Header