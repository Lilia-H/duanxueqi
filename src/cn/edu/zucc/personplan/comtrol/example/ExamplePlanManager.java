package cn.edu.zucc.personplan.comtrol.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.zucc.personplan.itf.IPlanManager;
import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.BusinessException;
import cn.edu.zucc.personplan.util.DBUtil;
import cn.edu.zucc.personplan.util.DbException;

public class ExamplePlanManager implements IPlanManager {

	@Override
	public BeanPlan addPlan(String name) throws BaseException {
		// TODO Auto-generated method stub
		if(name==null|| "".equals(name)) throw new BusinessException("计划名称必须提供");
		
		Connection conn=null;
		try {
			conn=DBUtil.getConnection();
			String user_id = BeanUser.currentLoginUser.getUserid();
			int plan_ord = 0;
			String sql="select * from  tbl_plan where user_id = ? and plan_name = ? ";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1, user_id);
			java.sql.ResultSet rs=pst.executeQuery();
			if(rs.next()) {
				rs.close();
				pst.close();
				throw new BusinessException("同名计划已存在");
				
			}
			rs.close();
			pst.close(); 
			
			sql="select max(plan_ord) from tbl_plan where user_id = ? ";
			pst=conn.prepareStatement(sql);
			pst.setString(1, user_id);
			rs=pst.executeQuery();
			if(rs.next()) {
				plan_ord=rs.getInt(1)+1;
				
			}
			else {
				plan_ord=1;
			}
			rs.close();
			pst.close();
			 sql ="insert into tbl_plan  (" + 
					"  user_id," + 
					"  plan_order," + 
					"  plan_name," + 
					"  create_time," + 
					"  step_count," + 
					"  start_step_count," + 
					"  finished_step_count)"+"vlaues(?,?,?,?,0,0,0)";
			 pst=conn.prepareStatement(sql);
				pst.setString(1, user_id);
				pst.setInt(2, plan_ord);
				pst.setString(3, name);
				pst.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
				pst.execute();
				BeanPlan p = new BeanPlan();
				sql="select max(plan_id) from tbl_plan where user_id = ? ";
				pst=conn.prepareStatement(sql);
				pst.setString(1, user_id);
				rs=pst.executeQuery();
				if(rs.next()) {
					int pid=rs.getInt(1);
				}else {
					
				}
				rs.close();
				pst.close();
				//p属性设置
				return p;
				
				
		}catch(SQLException ex) {
			throw  new DbException(ex);
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<BeanPlan> loadAll() throws BaseException {
		List<BeanPlan> result=new ArrayList<BeanPlan>();
		BeanPlan p=new BeanPlan();
		result.add(p);
		return result;
	}

	@Override
	public void deletePlan(BeanPlan plan) throws BaseException {
		
	}

}
