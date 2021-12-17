package com.example.remind.presentation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.remind.BR
import com.example.remind.R
import com.example.remind.databinding.RemindMainListItemRowBinding
import com.example.remind.model.MainViewModel
import com.example.remind.model.entity.RemindInfoEntity

class RemindListAdapter internal constructor(context: Context): RecyclerView.Adapter<RemindListAdapter.RemindViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var remindList = emptyList<RemindInfoEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindViewHolder {
        val itemView = inflater.inflate(R.layout.remind_main_list_item_row, parent, false)
        return RemindViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RemindViewHolder, position: Int) {
        val item = remindList[position]

        holder.bind(item)

        holder.alarmOnOff?.setOnCheckedChangeListener { checkBox, isChecked ->
            Log.w("KKC_TAG", "isChecked : $isChecked, item : $item")
        }
    }

    override fun getItemCount() = remindList.size

    internal fun setRemindList(remindList: List<RemindInfoEntity>){
        this.remindList = remindList
        notifyDataSetChanged()
    }

    inner class RemindViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding: RemindMainListItemRowBinding? = DataBindingUtil.bind(itemView)

        val alarmOnOff = binding?.cbAlarmStatus

        fun bind(entity: RemindInfoEntity){
            binding?.setVariable(BR.entity, entity)
        }
    }
}