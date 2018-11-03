import React, {Component} from 'react';

class CommentItem extends Component {

    render() {
        return (
            <div className='comment'>
                <div className='comment-user'>
                    <span>{this.props.comment333.username}</span> ：
                </div>
                <p>{this.props.comment333.content}</p>
            </div>
        )
    }
}

export default CommentItem;