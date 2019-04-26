package com.hang.api;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hang.annotation.OpenId;
import com.hang.enums.ResultEnum;
import com.hang.pojo.data.ProjectDO;
import com.hang.pojo.vo.BaseRes;
import com.hang.pojo.vo.GroupMemberVO;
import com.hang.pojo.vo.ProjectVO;
import com.hang.pojo.vo.TeamVO;
import com.hang.service.TeamService;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author hangs.zhang
 * @date 2019/1/25
 * *****************
 * function:
 */
@Slf4j
@Api("双创团队接口")
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
     * 无差别获取team
     *
     * @return
     */
    @ApiOperation("无差别获取团队信息")
    @GetMapping("/getAllTeam")
    public BaseRes getAllTeam(@RequestParam(required = false, defaultValue = "0") int start,
                              @RequestParam(required = false, defaultValue = "10") int offset) {
        return RespUtil.success(teamService.getTeams(start, offset));
    }

    /**
     * 根据openId返回teamList，projectList
     *
     * @param openId
     * @return
     */
    @ApiOperation("获取用户自己的团队以及项目信息")
    @GetMapping("/getTeamHomePage")
    public BaseRes getTeamHomePage(@OpenId String openId) {
        log.info("openId : {}", openId);
        if (StringUtils.isBlank(openId)) {
            return RespUtil.error(ResultEnum.CAN_NOT_GET_OPEN_ID);
        }

        HashMap<String, Object> result = Maps.newHashMap();
        List<TeamVO> teams = teamService.getTeamByUserId(openId);
        result.put("teams", teams);
        Set<ProjectVO> projects = Sets.newHashSet();
        teams.forEach(e -> projects.addAll(e.getProjects()));
        result.put("projects", projects);
        return RespUtil.success(result);
    }

    /**
     * 根据userId查询团队数据
     *
     * @param openId
     * @return
     */
    @ApiOperation("查询用户所属的团队")
    @GetMapping("/getTeamByUserId")
    public BaseRes getTeamByUserId(@OpenId String openId) {
        if (StringUtils.isBlank(openId)) return RespUtil.error(ResultEnum.CAN_NOT_GET_OPEN_ID);
        List<TeamVO> teamVOS = teamService.getTeamByUserId(openId);
        return RespUtil.success(teamVOS);
    }

    /**
     * 根据teamId查询团队数据
     *
     * @param teamId
     * @return
     */
    @ApiOperation("根据teamId查询team")
    @GetMapping("/getTeamByTeamId")
    public BaseRes getTeamByTeamId(@RequestParam int teamId) {
        TeamVO teamById = teamService.getTeamByTeamId(teamId);
        return RespUtil.success(teamById);
    }

    /********成员的curd********/

    /**
     * 增加成员
     *
     * @param teamId
     * @param groupMemberVO
     * @return
     */
    @ApiOperation("team添加成员")
    @GetMapping("/addMember2Team")
    public BaseRes addMember2Team(@RequestParam int teamId, @ModelAttribute GroupMemberVO groupMemberVO) {
        teamService.addMember2Team(teamId, groupMemberVO);
        return RespUtil.success();
    }

    /**
     * 增加项目user
     *
     * @param teamId
     * @param openId
     * @return
     */
    @ApiOperation("项目添加成员")
    @GetMapping("/addUser2Team")
    public BaseRes addUser2Team(int teamId, @OpenId String openId) {
        if (StringUtils.isBlank(openId)) return RespUtil.error(ResultEnum.CAN_NOT_GET_OPEN_ID);
        teamService.addUser2Team(teamId, openId);
        return RespUtil.success();
    }

    /**
     * 直接更新
     *
     * @param teamId
     * @param groupMemberVOS
     * @return
     */
    @ApiOperation("直接更新team的团队信息")
    @GetMapping("/updateMember2Team")
    public BaseRes updateMember2Team(@RequestParam int teamId, @RequestParam ArrayList<GroupMemberVO> groupMemberVOS) {
        teamService.updateMember2Team(teamId, groupMemberVOS);

        return RespUtil.success();
    }

    /********项目的curd********/

    /**
     * 创建project，然后添加到team
     *
     * @param teamId
     * @param projectName
     * @param projectDescription
     * @return
     */
    @ApiOperation("为team添加项目")
    @GetMapping("/addProject2Team")
    public BaseRes addProject2Team(@RequestParam int teamId, String projectName, String projectDescription) {
        ProjectDO projectDO = new ProjectDO();
        projectDO.setDescription(projectDescription);
        projectDO.setName(projectName);
        teamService.addProject2Team(teamId, projectDO);
        return RespUtil.success();
    }

    /**
     * 增加荣誉
     *
     * @param teamId
     * @param honor
     * @return
     */
    @ApiOperation("team添加荣耀")
    @GetMapping("/addHonor2Team")
    public BaseRes addHonor2Team(int teamId, String honor) {
        teamService.addHonor2Team(teamId, honor);
        return RespUtil.success();
    }

    /**
     * 追加日志
     *
     * @param teamId
     * @param log
     * @return
     */
    @ApiOperation("team添加日志")
    @GetMapping("/addLog2Team")
    public BaseRes addLog2Team(int teamId, String log) {
        teamService.addLog2Team(teamId, log);
        return RespUtil.success();
    }


}