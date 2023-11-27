package com.uniandes.abcjobs.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uniandes.abcjobs.databinding.CandidateSearchItemBinding
import androidx.databinding.DataBindingUtil
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.uniandes.abcjobs.R
import com.uniandes.abcjobs.models.CandidateRequestSearch
import com.uniandes.abcjobs.models.CandidateResponseSearch
import com.uniandes.abcjobs.models.ProjectMemberRequest

class CandidateSearchAdapter(private val candidates: List<CandidateResponseSearch>, project: String?,
profile: String?, private val onAddMemberCallback: (ProjectMemberRequest) -> Unit) : RecyclerView.Adapter<CandidateSearchAdapter.CandidateSearchViewHolder>(){
    private var projectId= project
    private var profileId= profile

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateSearchViewHolder{

        val withDataBinding: CandidateSearchItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CandidateSearchViewHolder.LAYOUT,
            parent,
            false
        )
        return CandidateSearchViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CandidateSearchViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.candidate = candidates[position]
        }
        holder.bind(candidates[position])
        holder.viewDataBinding.root.setOnClickListener {
            val candidate_pos = candidates[position]
            Log.d("********* Person Id:", candidate_pos.person_id.toString())
            /*val activity = holder.itemView.context
            val i = Intent(activity, ArtistDetailsActivity::class.java)
            i.putExtra("id", Id.id.toString())
            i.putExtra("name", Id.name)
            activity.startActivity(i)*/
        }
        holder.viewDataBinding.switch1.setOnCheckedChangeListener{ _ , isChecked ->
            if (isChecked) {
                // The switch is enabled/checked
                println("************ Asignado al proyecto ${projectId} con perfil ${profileId} person ${candidates[position].person_id}")
                val memberRequest = ProjectMemberRequest(
                    "1",
                    "",
                    candidates[position].person_id.toString(),
                    profileId.toString(),
                    projectId.toString()
                )
                onAddMemberCallback(memberRequest)
            } else {
                println("************* No Asignado al proyecto")
            }
        }
    }

    override fun getItemCount(): Int {
        return candidates.size
    }

    class CandidateSearchViewHolder(val viewDataBinding: CandidateSearchItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.candidate_search_item
        }

        fun bind(candidate: CandidateResponseSearch) {
            println("********Haciendo binding")
            viewDataBinding.score.text = candidate.score.toString()
            viewDataBinding.nameCandidate.text = "${candidate.first_name}  ${candidate.last_name}"
            viewDataBinding.academic.text = candidate.titles
        }

    }
}