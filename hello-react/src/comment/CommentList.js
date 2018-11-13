import React from 'react';
import BaseComponent from './BaseComponent.js'
import CommentItem from "./CommentItem";

class CommentList extends BaseComponent {

    static defaultProps = {
        comments: []
    }

    handleDeleteComment(index) {
        if (this.props.onDeleteComment) {
            this.props.onDeleteComment(index)
        }
    }

    render() {
        return (
            <div>
                {this.props.comments.map((comment, i) =>
                    <CommentItem
                        comment={comment}
                        key={i}
                        index={i}
                        onDeleteComment={this.handleDeleteComment.bind(this)}/>
                )}
            </div>
        )
    }
}

export default CommentList;