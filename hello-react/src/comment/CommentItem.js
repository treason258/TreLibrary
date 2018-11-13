import React, {Component} from 'react';
import PropTypes from 'prop-types'

class CommentItem extends Component {

    static propTypes = {
        comment: PropTypes.object
    }

    render() {
        const { comment333 } = this.props
        return (
            <div className='comment'>
                <div className='comment-user'>
                    <span>{comment333.username}</span> ：
                </div>
                <p>{comment333.content}</p>
            </div>
        )
    }
}

export default CommentItem;