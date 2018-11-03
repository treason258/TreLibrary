import React, {Component} from 'react';
import CommentInput from './CommentInput.js'
import CommentList from './CommentList.js'
import './Comment.css';

class CommentApp extends Component {

    constructor() {
        super()
        this.state = {
            comments444: []
        }
    }

    handleSubmitComment(comment) {
        console.log(comment)
        if (!comment) return
        if (!comment.username) return alert('请输入用户名')
        if (!comment.content) return alert('请输入评论内容')
        this.state.comments444.push(comment)
        this.setState({
            comments444: this.state.comments444
        })
    }

    render() {
        return (
            <div className='wrapper'>
                <CommentInput
                    onSubmit111={this.handleSubmitComment.bind(this)}
                />
                <CommentList
                    comments111={this.state.comments444}
                />
            </div>
        )
    }
}

export default CommentApp;