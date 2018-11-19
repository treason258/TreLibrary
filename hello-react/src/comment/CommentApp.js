import React from 'react';
import BaseComponent from './BaseComponent.js'
import CommentInput from './components/CommentInput.js'
import CommentList from './components/CommentList.js'
import './Comment.css';
import wrapWithLocalStorage from './LocalStorageActions'

class CommentApp extends BaseComponent {

    constructor(props) {
        super(props)
        this.state = {
            comments: props.data
        }
    }

    // componentWillMount() {
    //     super.componentWillMount()
    //     this._loadComments()
    // }

    // _saveComments(comments) {
    //     let commentsString = JSON.stringify(comments)
    //     console.log('_saveComments - ' + commentsString)
    //     localStorage.setItem('comments', commentsString)
    // }

    // _loadComments() {
    //     console.log('_loadComments - ' + localStorage.getItem('comments'))
    //     let commentString = localStorage.getItem('comments')
    //     if (commentString) {
    //         let comments = JSON.parse(commentString)
    //         console.log('_loadComments222 - ' + comments)
    //         this.setState({comments})
    //     }
    // }

    handleSubmitComment(comment) {
        console.log('handleSubmitComment | ' + JSON.stringify(comment))
        if (!comment) return
        if (!comment.username) return alert('请输入用户名')
        if (!comment.content) return alert('请输入评论内容')
        const comments = this.state.comments
        comments.push(comment)
        this.setState({comments})
        // this._saveComments(comments)
        this.props.saveData(comments)
    }

    handleDeleteComment(index) {
        console.log('handleDeleteComment | index -> ' + index)
        const comments = this.state.comments
        comments.splice(index, 1)
        this.setState({comments})
        // this._saveComments(comments)
        this.props.saveData(comments)
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

CommentApp = wrapWithLocalStorage(CommentApp, 'comments')
export default CommentApp;