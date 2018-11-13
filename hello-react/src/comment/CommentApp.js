import React from 'react';
import BaseComponent from './BaseComponent.js'
import CommentInput from './CommentInput.js'
import CommentList from './CommentList.js'
import './Comment.css';

class CommentApp extends BaseComponent {

    constructor() {
        super()
        this.state = {
            comments: []
        }
    }

    componentWillMount() {
        super.componentWillMount()
        this._loadComments()
    }

    _saveComments(comments) {
        let commentsString = JSON.stringify(comments)
        console.log('_saveComments - ' + commentsString)
        localStorage.setItem('comments', commentsString)
    }

    _loadComments() {
        console.log('_loadComments - ' + localStorage.getItem('comments'))
        let commentString = localStorage.getItem('comments')
        if (commentString) {
            let comments = JSON.parse(commentString)
            console.log('_loadComments222 - ' + comments)
            this.setState({comments})
        }
    }

    handleSubmitComment(comment) {
        console.log('handleSubmitComment | ' + JSON.stringify(comment))
        if (!comment) return
        if (!comment.username) return alert('请输入用户名')
        if (!comment.content) return alert('请输入评论内容')
        const comments = this.state.comments
        comments.push(comment)
        this.setState({comments})
        this._saveComments(comments)
    }

    handleDeleteComment(index) {
        console.log('handleDeleteComment | index -> ' + index)
        const comments = this.state.comments
        comments.splice(index, 1)
        this.setState({comments})
        this._saveComments(comments)
    }

    render() {
        return (
            <div className='wrapper'>
                <CommentInput
                    onSubmit={this.handleSubmitComment.bind(this)}

                />
                <CommentList
                    comments={this.state.comments}
                    onDeleteComment={this.handleDeleteComment.bind(this)}
                />
            </div>
        )
    }
}

export default CommentApp;