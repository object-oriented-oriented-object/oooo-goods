package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.Status;
import cn.edu.xmu.goods.model.StatusWrap;
import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.vo.CommentConfirmVo;
import cn.edu.xmu.goods.model.vo.CommentVo;
import cn.edu.xmu.goods.service.CommentService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-08 11:05
 */
@RestController
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class CommentController {
    @Autowired
    private CommentService commentService;
//
    @ApiOperation(value = "获得评论的所有状态", produces = "application/json;charset=UTF-8")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping(path = "/comments/states")
    public ResponseEntity<StatusWrap> getCommentState() {
        return StatusWrap.of(Arrays.asList(Comment.State.values()));
    }

    @ApiOperation(value = "查看sku的评价列表（已通过审核）", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "sku id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页数目", required = false)

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/skus/{id}/comments")
    public Object getSkuComments(@PathVariable Long id,
                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return commentService.getSkuComments(id, page, pageSize);
    }

    @Audit
    @ApiOperation(value = "买家查看自己的评价记录", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/comments")
    public Object getSelfComments(@LoginUser Long userId,
                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        //用户信息
        //Long userId= Long.valueOf(1);
        if (userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return commentService.getSelfComments(userId, page, pageSize);
    }

    @Audit
    @ApiOperation(value = "管理员查看未审核/已审核评价列表", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "shop id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "state", value = "状态", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页数目", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/shops/{id}/comments/all")
    public Object getShopComments(@LoginUser @ApiIgnore Long userId,
                                  @PathVariable("id") Long id,
                                  @Depart @ApiIgnore Long departId,
                                  @RequestParam(required = false) Integer state,
                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        if (userId == null || departId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!id.equals(departId) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        return commentService.getShopComments(state, page, pageSize);
    }

    @Audit
    @ApiOperation(value = "买家新增sku的评论", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "CommentVo", name = "vo", value = "评价信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 903, message = "用户没有购买此商品")
    })
    @PostMapping("/orderitems/{id}/comments")
    public Object createComment(@RequestBody CommentVo vo,
                                @Depart @ApiIgnore Long departId,
                                @LoginUser @ApiIgnore Long userId,
                                @PathVariable Long id) {
        if (userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        return commentService.createComment(userId, id, vo);
    }

    @Audit
    @ApiOperation(value = "管理员审核评论", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "did", value = "shop id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "comment id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "CommentConfirmVo", name = "vo", value = "可修改的评论信息", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @PutMapping("/shops/{did}/comments/{id}/confirm")
    public Object confirmComment(@LoginUser @ApiIgnore Long userId,
                                 @PathVariable Long id,
                                 @PathVariable Long did,
                                 @Depart @ApiIgnore Long departId,
                                 @RequestBody(required = false) CommentConfirmVo vo) {
        if (departId == null || userId == null)
            return StatusWrap.just(Status.LOGIN_REQUIRED);
        if (!departId.equals(did) && departId != 0) {
            return StatusWrap.just(Status.RESOURCE_ID_OUTSCOPE);
        }
        if (vo == null) {
            return StatusWrap.just(Status.FIELD_NOTVALID);
        }
        return commentService.confirmComment(id, vo);
    }
}

















