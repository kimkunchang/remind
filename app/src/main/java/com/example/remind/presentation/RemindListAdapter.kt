package com.example.remind.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.remind.BR
import com.example.remind.R
import com.example.remind.databinding.RemindMainListItemRowBinding
import com.example.remind.model.entity.RemindInfoEntity

class RemindListAdapter : RecyclerView.Adapter<RemindListAdapter.RemindViewHolder>() {

    private var remindList = emptyList<RemindInfoEntity>()
    private var mListener: OnStatusRemindListener? = null

    fun setOnStatusRemindListener(listener: OnStatusRemindListener){
        this.mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.remind_main_list_item_row, parent, false)
        return RemindViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RemindViewHolder, position: Int) {
        val item = remindList[position]
        holder.bind(item)
    }

    override fun getItemCount() = remindList.size

    internal fun setRemindList(remindList: List<RemindInfoEntity>){
        this.remindList = remindList
        notifyDataSetChanged()
    }

    inner class RemindViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding: RemindMainListItemRowBinding? = DataBindingUtil.bind(itemView)

        fun bind(entity: RemindInfoEntity){
            binding?.setVariable(BR.entity, entity)

            binding?.cbAlarmStatus?.setOnClickListener {
                mListener?.onStatusChange(entity)
            }

            binding?.root?.setOnClickListener {
                it.findNavController().navigate(RemindMainFragmentDirections.actionRemindSettingFragment(entity.alarmID))
            }
        }
    }
}